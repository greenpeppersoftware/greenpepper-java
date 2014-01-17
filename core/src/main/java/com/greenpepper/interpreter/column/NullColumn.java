/**
 * 
 */
package com.greenpepper.interpreter.column;

import com.greenpepper.Example;
import com.greenpepper.Statistics;

public class NullColumn extends Column
{
    public Statistics doCell( Example cell )
    {
        return new Statistics();
    }
}