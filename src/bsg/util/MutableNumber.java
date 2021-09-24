package bsg.util;

public interface MutableNumber {
   public MutableNumber set(Number i);
   public MutableNumber add(Number i);
   public MutableNumber sub(Number i);
   public double doubleValue();
   public float floatValue();
   public long longValue();
   public int intValue();
}
