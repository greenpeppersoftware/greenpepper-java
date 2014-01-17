package com.greenpepper.converter;

import com.greenpepper.GreenPepper;
import com.greenpepper.TypeConversion;
import com.greenpepper.util.ClassUtils;
import com.greenpepper.util.NameUtils;

public class TypeConversionFixture
{
    private Class type;

    public TypeConversionFixture()
    {
        GreenPepper.register(new TypeConversionFixtureConverter());
    }

    private static Class getClass(String typename) throws Exception
    {
        return ClassUtils.loadClass("com.greenpepper.converter.TypeConversionFixture$"+NameUtils.toClassName(typename));
     }

    public String thatConvertingToUses(String typeName) throws Exception
    {
        TypeConverter converter = TypeConversion.converterForType(ClassUtils.loadClass(typeName));
        return converter.getClass().getSimpleName();
    }

    public void register(String converterName) throws Exception
    {
        Class clazz = ClassUtils.loadClass(converterName);
        GreenPepper.register((TypeConverter)clazz.newInstance());
    }

    public void type(String typename) throws Exception
    {

        type = getClass(typename);
    }

    public String parseWith()
    {
        return String.valueOf(TypeConversion.parse("xcare", type));
    }

    public String toStringWith() throws Throwable
    {
        return TypeConversion.toString(type.newInstance());
    }

    public static class TypeConversionFixtureConverter implements TypeConverter
    {
        public boolean canConvertTo(Class type)
        {
            return TypeWithConverterNoSelf.class.isAssignableFrom(type) || TypeWithConverterWithSelf.class.isAssignableFrom(type);
        }

        public Object parse(String value, Class type)
        {
            return "converter.parse";
        }

        public String toString(Object value)
        {
            return "converter.toString";
        }
    }

    public static class TypeNoConverterNoSelf
    {
        public String toString()
        {
            return "class.toString";
        }
    }

    public static class TypeNoConverterWithSelf
    {
        private String toStringValue = "class.toString";

        public TypeNoConverterWithSelf() {}

        private TypeNoConverterWithSelf(String toStringValue)
        {
            this.toStringValue = toStringValue;
        }

        public static TypeNoConverterWithSelf parse(String value)
        {
            return new TypeNoConverterWithSelf("self.parse");
        }
        
        public static TypeNoConverterWithSelf valueOf(String value)
        {
            return new TypeNoConverterWithSelf("self.valueOf");
        }

        public static String toString(TypeNoConverterWithSelf value)
        {
            return "self.toString";
        }

        public String toString()
        {
            return toStringValue;
        }
    }
    
    public static class TypeNoConverterWithSelfByValueOf
    {
        private String toStringValue = "class.toString";

        public TypeNoConverterWithSelfByValueOf() {}

        private TypeNoConverterWithSelfByValueOf(String toStringValue)
        {
            this.toStringValue = toStringValue;
        }

        public static TypeNoConverterWithSelfByValueOf valueOf(String value)
        {
            return new TypeNoConverterWithSelfByValueOf("self.valueOf");
        }

        public static String toString(TypeNoConverterWithSelf value)
        {
            return "self.toString";
        }

        public String toString()
        {
            return toStringValue;
        }
    }

    public static class TypeWithConverterNoSelf
    {
        public String toString()
        {
            return "class.toString";
        }
    }

    public static class TypeWithConverterWithSelf
    {
        private String toStringValue = "class.toString";

        public TypeWithConverterWithSelf() {}

        private TypeWithConverterWithSelf(String toStringValue)
        {
            this.toStringValue = toStringValue;
        }

        public static TypeWithConverterWithSelf parse(String value)
        {
            return new TypeWithConverterWithSelf("self.parse");
        }

        public static TypeWithConverterWithSelf valueOf(String value)
        {
            return new TypeWithConverterWithSelf("self.valueOf");
        }

        public static String toString(TypeWithConverterWithSelf value)
        {
            return "self.toString";
        }

        public String toString()
        {
            return toStringValue;
        }
    }
}
