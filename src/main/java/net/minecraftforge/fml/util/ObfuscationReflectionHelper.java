package net.minecraftforge.fml.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Minimal Fabric port stub for the Forge reflection helper.
 * Implements only the methods used by FramedBlocks.
 */
public final class ObfuscationReflectionHelper
{
    public static <T, E> T getPrivateValue(Class<? super E> classToAccess, E instance, String... fieldNames)
    {
        for (String fieldName : fieldNames)
        {
            try
            {
                Field field = classToAccess.getDeclaredField(fieldName);
                field.setAccessible(true);
                return (T) field.get(instance);
            }
            catch (NoSuchFieldException ignored) { }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException("Unable to access field '" + fieldName + "' in " + classToAccess.getName(), e);
            }
        }
        throw new UnableToAccessFieldException("Unable to find any of the specified fields in " + classToAccess.getName());
    }

    public static Field findField(Class<?> clazz, String... fieldNames)
    {
        for (String fieldName : fieldNames)
        {
            try
            {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            }
            catch (NoSuchFieldException ignored) { }
        }
        throw new UnableToAccessFieldException("Unable to find any of the specified fields in " + clazz.getName());
    }

    public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes)
    {
        try
        {
            Method method = clazz.getDeclaredMethod(name, paramTypes);
            method.setAccessible(true);
            return method;
        }
        catch (NoSuchMethodException e)
        {
            throw new UnableToAccessFieldException("Unable to find method '" + name + "' in " + clazz.getName(), e);
        }
    }

    private ObfuscationReflectionHelper() { }

    public static final class UnableToAccessFieldException extends RuntimeException
    {
        public UnableToAccessFieldException(String message) { super(message); }
        public UnableToAccessFieldException(String message, Throwable cause) { super(message, cause); }
    }
}
