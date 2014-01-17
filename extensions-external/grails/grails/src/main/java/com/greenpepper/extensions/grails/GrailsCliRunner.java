/**
 * Copyright (c) 2009 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */
package com.greenpepper.extensions.grails;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.greenpepper.GreenPepper;
import com.greenpepper.Statistics;
import com.greenpepper.document.GreenPepperInterpreterSelector;
import com.greenpepper.document.InterpreterSelector;
import com.greenpepper.report.FileReportGenerator;
import com.greenpepper.report.PlainReport;
import com.greenpepper.report.ReportGenerator;
import com.greenpepper.report.XmlReport;
import com.greenpepper.repository.DocumentRepository;
import com.greenpepper.repository.FileSystemRepository;
import com.greenpepper.runner.CompositeSpecificationRunnerMonitor;
import com.greenpepper.runner.DocumentRunner;
import com.greenpepper.runner.LoggingMonitor;
import com.greenpepper.runner.RecorderMonitor;
import com.greenpepper.runner.SpecificationRunner;
import com.greenpepper.runner.SpecificationRunnerMonitor;
import com.greenpepper.runner.SuiteRunner;
import com.greenpepper.systemunderdevelopment.SystemUnderDevelopment;
import com.greenpepper.util.Bean;
import com.greenpepper.util.IOUtil;
import static com.greenpepper.util.URIUtil.decoded;
import static com.greenpepper.util.URIUtil.flatten;

public class GrailsCliRunner
		implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	private boolean suite;
	private String locale;
	private String input;
	private String output;
	private String outputType = "html";
	private boolean debug;
	private boolean failOnError = true;
	private boolean stopOnFirstFailure;
	private String[] sections = new String[0];

	private SystemUnderDevelopment systemUnderDevelopment;
	private DocumentRepository documentRepository;
	private SpecificationRunnerMonitor monitor = new LoggingMonitor(System.out, System.err);
	private final RecorderMonitor recorderMonitor = new RecorderMonitor();
	private Class<? extends InterpreterSelector> interpreterSelectorClass = GreenPepperInterpreterSelector.class;

	public GrailsCliRunner() {
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public boolean isSuite() {
		return suite;
	}

	public void setSuite(boolean suite) {
		this.suite = suite;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getOutputType() {
		return outputType;
	}

	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isFailOnError() {
		return failOnError;
	}

	public void setFailOnError(boolean failOnError) {
		this.failOnError = failOnError;
	}

	public boolean isStopOnFirstFailure() {
		return stopOnFirstFailure;
	}

	public void setStopOnFirstFailure(boolean stopOnFirstFailure) {
		this.stopOnFirstFailure = stopOnFirstFailure;
	}

	public String[] getSections() {
		return sections;
	}

	public void setSections(String[] sections) {
		this.sections = sections;
	}

	public SystemUnderDevelopment getSystemUnderDevelopment() {
		return systemUnderDevelopment;
	}

	public void setSystemUnderDevelopment(SystemUnderDevelopment systemUnderDevelopment) {
		this.systemUnderDevelopment = systemUnderDevelopment;
	}

	public DocumentRepository getDocumentRepository() {
		return documentRepository;
	}

	public void setDocumentRepository(DocumentRepository documentRepository) {
		this.documentRepository = documentRepository;
	}

	public SpecificationRunnerMonitor getMonitor() {
		return monitor;
	}

	public void setMonitor(SpecificationRunnerMonitor monitor) {
		this.monitor = monitor;
	}

	public Class<? extends InterpreterSelector> getInterpreterSelectorClass() {
		return interpreterSelectorClass;
	}

	public void setInterpreterSelectorClass(Class<? extends InterpreterSelector> interpreterSelectorClass) {
		this.interpreterSelectorClass = interpreterSelectorClass;
	}

	public RecorderMonitor getRecorderMonitor()
	{
		return recorderMonitor;
	}

	public boolean hasTestFailures()
	{
		return getRecorderMonitor().hasTestFailures();
	}

	public boolean hasException()
	{
		return getRecorderMonitor().hasException();
	}

	public Statistics getStatistics()
	{
		return getRecorderMonitor().getStatistics();
	}

	public void run()
			throws Exception {

		if (locale != null)
		{
			GreenPepper.setLocale(new Locale(locale));
		}

		final boolean previousDebugMode = GreenPepper.isDebugEnabled();
		GreenPepper.setDebugEnabled(debug);

		final boolean previousStopMode = GreenPepper.isStopOnFirstFailure();
		GreenPepper.setStopOnFirstFailure(stopOnFirstFailure);

		try {
			log("\n-------------------------------------------------------");
			log(" G R E E N  P E P P E R  S P E C I F I C A T I O N S   ");
			log("-------------------------------------------------------");

			SpecificationRunner runner = suite ? new SuiteRunner() : new DocumentRunner();

			Map<String, Object> options = new HashMap<String, Object>();

			options.put("systemUnderDevelopment", systemUnderDevelopment());
			options.put("report generator", reportGenerator());
			options.put("monitor", monitor());
			options.put("repository", repository());
			options.put("sections", getSections());
			options.put("interpreter selector", getInterpreterSelectorClass());

			new Bean(runner).setProperties(options);

			runner.run(source(), destination());

			log(String.format("Results: %s for %s specification(s)",
							  recorderMonitor.getStatistics().toString(),
							  recorderMonitor.getLocationCount()));

			checkResults(recorderMonitor.hasException(), "Some greenpepper tests did not run");
			checkResults(recorderMonitor.hasTestFailures(), "There were greenpepper tests failures");
		}
		finally {

			if (previousDebugMode != GreenPepper.isDebugEnabled()) {
				GreenPepper.setDebugEnabled(previousDebugMode);
			}
			if (previousStopMode != GreenPepper.isStopOnFirstFailure()) {
				GreenPepper.setStopOnFirstFailure(previousStopMode);
			}
		}
	}

	private void checkResults(boolean state, String message)
	{
		if (state)
		{
			if (failOnError)
			{
				throw new RuntimeException(message);
			}
			else
			{
				log(String.format("\nWarning : %s\n", message));
			}
		}
	}

	private SystemUnderDevelopment systemUnderDevelopment()
	{
		if (systemUnderDevelopment == null)
		{
			// Fallback to default found in the plugin
			systemUnderDevelopment = (SystemUnderDevelopment)applicationContext.getBean("systemUnderDevelopment");
		}

		return systemUnderDevelopment;
	}

	private SpecificationRunnerMonitor monitor()
	{
		return new CompositeSpecificationRunnerMonitor(recorderMonitor, monitor);
	}

	private DocumentRepository repository()
			throws IOException {
		return documentRepository != null ? documentRepository : new FileSystemRepository(parentFile(input()));
	}

	private ReportGenerator reportGenerator()
			throws IOException {
		FileReportGenerator generator = new FileReportGenerator(createOuputDirectory());
		generator.adjustReportFilesExtensions(suite || output() == null);
		generator.setReportClass("xml".equalsIgnoreCase(outputType) ? XmlReport.class : PlainReport.class);
		return generator;
	}

	private File createOuputDirectory()
			throws IOException {
		return IOUtil.createDirectoryTree(outputDirectory());
	}

	private String input() {
		return getInput() != null ? decoded(getInput()) : null;
	}

	private String output() {
		return getOutput() != null ? decoded(getOutput()) : null;
	}

	private String source() {
		return documentRepository != null ? input() : fileName(input());
	}

	private File outputDirectory()
			throws IOException {

		String output = output();

		if (output == null) {
			return new File(getOutput());
		}

		return suite ? new File(output) : parentFile(output);
	}

	private File parentFile(String pathname)
			throws IOException {
		return new File(pathname).getCanonicalFile().getParentFile();
	}

	private String fileName(String pathname) {
		return new File(pathname).getName();
	}

	private String destination()
			throws IOException {
		
		if (suite) {
			return "";
		}

		String output = output();

		if (output != null) {
			return fileName(output);
		}

		return documentRepository != null ? flatten(input()) : fileName(input());
	}

	private void log(String message)
	{
		System.out.println(message);
	}
}