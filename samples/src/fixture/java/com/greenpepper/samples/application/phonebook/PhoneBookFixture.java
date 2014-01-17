package com.greenpepper.samples.application.phonebook;

import java.util.List;

import com.greenpepper.samples.application.phonebook.PhoneBook;
import com.greenpepper.samples.application.phonebook.PhoneBookEntry;
import com.greenpepper.samples.application.phonebook.hibernate.PhoneBookMemoryDatabase;

public class PhoneBookFixture
{
    private PhoneBookMemoryDatabase db;
    private PhoneBook phoneBook;
	
	public PhoneBookFixture() throws Exception
	{
    	db = new PhoneBookMemoryDatabase();
    	db.mount();
    	setUp();
	}
	
    public void insertWithNumber(String firstName, String lastName, String number)
    {
    	db.startSession();
    	db.beginTransaction();
        phoneBook.insert(firstName, lastName, number);
        db.commitTransaction();
    }

    public List<PhoneBookEntry> phoneBookEntries() 
    {
    	db.startSession();
    	db.beginTransaction();
        return phoneBook.getEntries();
    }
    
    private void setUp() throws Exception
    {    	
    	db.startSession();
    	db.beginTransaction(); 
    	phoneBook = new PhoneBook("My Phone Book");
    	db.commitTransaction();
    }
}
