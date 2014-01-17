package com.greenpepper.extensions.selenium;

import com.greenpepper.Specification;
import com.greenpepper.interpreter.ActionInterpreter;
import com.greenpepper.util.ExceptionImposter;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;

public class SeleniumInterpreter extends ActionInterpreter
{
    private final SeleniumFixture fixture;

    public SeleniumInterpreter(SeleniumFixture fixture)
    {
        super( fixture );
        this.fixture = fixture;
    }

    @Override
    public void interpret(Specification specification)
    {
        SeleniumServer seleniumServer = null;

        try
        {
            seleniumServer = new SeleniumServer( getRemoteControlConfiguration() );
            seleniumServer.start();
            startCommandProcessor();
            super.interpret( specification );

        }
        catch (Exception e)
        {
            throw ExceptionImposter.imposterize( e );
        }
        finally
        {
            stopCommandProcessor();
            stop( seleniumServer );
        }
    }

	private RemoteControlConfiguration getRemoteControlConfiguration()
	{
		RemoteControlConfiguration configuration = new RemoteControlConfiguration();
		configuration.setPort(4343);
		return configuration;
	}

    private void stopCommandProcessor()
    {
        fixture.getTarget().stop();
    }

    private void startCommandProcessor()
    {
        fixture.getTarget().start();
    }

    private void stop(SeleniumServer seleniumServer)
    {
        if (seleniumServer == null) return;

		seleniumServer.stop();
    }
}
