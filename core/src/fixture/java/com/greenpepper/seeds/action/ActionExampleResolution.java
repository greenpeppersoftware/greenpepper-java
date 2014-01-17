package com.greenpepper.seeds.action;

import java.util.GregorianCalendar;

@SuppressWarnings("unused")
public class ActionExampleResolution
{
   /*
    * For Action Access Resolution seeds.
    */
   public  int aPublicField;
   private int aPrivateField;

   public  int  getAPublicGetterAndSetterOnAField(){return 0;}
   public  void setAPublicGetterAndSetterOnAField(int m){}

   private int  getAPrivateGetterAndSetterOnAField(){return 0;}
   private void setAPrivateGetterAndSetterOnAField(int n){}

   public  void aPublicMethodWithNoParameterThatReturnNothing(){}
   public  void aPublicMethodWithOneParameterThatReturnNothing(String p){}
   private void aPrivateMethodThatReturnNothing(){}

   public  String aPublicMethodWithNoParameterThatReturnAValue(){return "v";}
   public  String aPublicMethodWithOneParameterThatReturnAValue(String p){return "v";}
   private String aPrivateMethodThatReturnAValue(){return "w";}

   /*
    * For Action Order Resolution seeds. 
    */
   public String personName(){return "personName";}
   public Person person = new Person();
   
   public class Person
   {
       public String name(){return "person.name";}
       public String nick(){return "person.nick";}
   }
   
   /*
    * For Action Expression Resolution seeds.
    */
   public String[] month  = {"Error", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
   public Year year = new Year();
   
   public Year newYear()
   {
       return new Year();
   }
   
   public String nameOfMonth(int monthNumber)
   {
       return month[monthNumber];
   }
   
   public int giveYear(int yr)
   {
       return yr;
   }
   
   public class Year
   {
       int year;
       
       public String firstMonth()
       {
           return month[1];
       }
       
       public String secondMonth()
       {
           return month[2];
       }
       
       public String[] getMonths()
       {
           return month;
       }
       
       public Year of(int year)
       {
           this.year = year;
           return this;
       }
       
       public boolean isLeapYear()
       {
           GregorianCalendar calendar = new GregorianCalendar();
           return calendar.isLeapYear(year);
       }
       
       @Override
       public String toString()
       {
           return "ActionExampleResolution$Year";
       }       
   }
}
