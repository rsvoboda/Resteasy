package org.jboss.resteasy.util;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Type conversions and generic type manipulations
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class Types
{
   public static Class<?> getRawType(Type type)
   {
      if (type instanceof Class<?>)
      {
         // type is a normal class.
         return (Class<?>) type;

      }
      else if (type instanceof ParameterizedType)
      {
         ParameterizedType parameterizedType = (ParameterizedType) type;
         Type rawType = parameterizedType.getRawType();
         return (Class<?>) rawType;
      }
      else if (type instanceof GenericArrayType)
      {
         final GenericArrayType genericArrayType = (GenericArrayType) type;
         final Class<?> componentRawType = getRawType(genericArrayType.getGenericComponentType());
         return Array.newInstance(componentRawType, 0).getClass();
      }
      throw new RuntimeException("Unable to determine base class from Type");
   }


   /**
    * Returns the type argument from a parameterized type
    *
    * @param genericType
    * @return null if there is no type parameter
    */
   public static Class<?> getTypeArgument(Type genericType)
   {
      if (!(genericType instanceof ParameterizedType)) return null;
      ParameterizedType parameterizedType = (ParameterizedType) genericType;
      Class<?> typeArg = (Class<?>) parameterizedType.getActualTypeArguments()[0];
      return typeArg;
   }


   public static class TypeInfo
   {
      private Class<?> type;
      private Type genericType;

      public TypeInfo(Class<?> type, Type genericType)
      {
         this.type = type;
         this.genericType = genericType;
      }

      public Class<?> getType()
      {
         return type;
      }

      public Type getGenericType()
      {
         return genericType;
      }
   }

   public static Class getCollectionBaseType(Class type, Type genericType)
   {
      if (genericType instanceof ParameterizedType)
      {
         ParameterizedType parameterizedType = (ParameterizedType) genericType;
         Type componentGenericType = parameterizedType.getActualTypeArguments()[0];
         return getRawType(componentGenericType);
      }
      else if (genericType instanceof GenericArrayType)
      {
         final GenericArrayType genericArrayType = (GenericArrayType) genericType;
         Type componentGenericType = genericArrayType.getGenericComponentType();
         return getRawType(componentGenericType);
      }
      else if (type.isArray())
      {
         return type.getComponentType();
      }
      return null;
   }


}
