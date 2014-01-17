/*
 * Copyright (c) 2007 Pyxis Technologies inc.
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
package com.greenpepper.interpreter;

import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.NoSuchMessageException;
import com.greenpepper.reflect.SystemUnderDevelopmentException;

import java.util.HashMap;

public class MockFixture implements Fixture
{
    public static class MockMessage extends Message
    {
        private String answer;

        public MockMessage(String answer)
        {
            this.answer = answer;
        }

        public int getArity()
        {
            return -1;
        }

        public Object send(String... args) throws SystemUnderDevelopmentException
        {
            return answerFrom( answer );
        }

        private Object answerFrom(String result) throws SystemUnderDevelopmentException
        {
            if (result.equals( "void" )) return null;
            if (result.equals( "true" )) return true;
            if (result.equals( "false" )) return false;
            if (result.equals( "error" ))
                throw new SystemUnderDevelopmentException( new Exception( "error asked, error returned!" ) );
            return result;
        }
    }

    HashMap<String, MockMessage> sends = new HashMap<String, MockMessage>();
    HashMap<String, MockMessage> checks = new HashMap<String, MockMessage>();

    private Object target;
    public String actionName;
    public String[] parameters;
    public Object result;

    public MockFixture()
    {
        this( null );
    }

    public MockFixture(Object target)
    {
        this.target = target;
    }

    public void willRespondTo(String message, String answer)
    {
        sends.put( message, new MockMessage( answer ) );
        checks.put( message, new MockMessage( answer ) );
    }

    public void willRespondToSend(String message, String answer)
    {
        sends.put( message, new MockMessage( answer ) );
    }

    public void willRespondToCheck(String message, String answer)
    {
        checks.put( message, new MockMessage( answer ) );
    }

    // -- Fixture implementation ---
    public boolean respondsTo(String message)
    {
        return canSend( message ) || canCheck( message );
    }

    public boolean canSend(String message)
    {
        return sends.containsKey( message );
    }

    public boolean canCheck(String message)
    {
        return checks.containsKey( message );
    }

    public Message check(String message) throws NoSuchMessageException
    {
        return checks.get( message );
    }

    public Message send(String message) throws NoSuchMessageException
    {
        return sends.get( message );
    }

    public Object getTarget()
    {
        return target;
    }

    public Fixture fixtureFor(Object target)
    {
        return new MockFixture( target );
    }
//-- End fixture implementation --

    public Message getter(String message) throws NoSuchMessageException
    {
        return checks.get( message );
    }

//  public Action mockAction = new Action() {
//  public int getArity() {
//      return 0;
//  }
//
//  public Object execute(String... args) throws Exception {
//      parameters = args;
//      if (result.equals("true")) return true;
//      if (result.equals("false")) return false;
//      if (result.equals("error")) throw new Exception();
//      return result;
//  }
//};
//
//public void willReturn(Object returnValue) {
//  result = returnValue;
//}
//
//public Action getQuery(String key) throws Exception {
//  return mock(key);
//}
//
//public Action getCommand(String key) throws Exception {
//  return mock(key);
//}
//
//private Action mock(String key) {
//  actionName = key;
//  return mockAction;
//}
}
