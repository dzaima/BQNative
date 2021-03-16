package BQNative;

import BQN.errors.*;
import BQN.tools.JBC;
import BQN.types.*;

public abstract class NType {
  
  public abstract Class<?> cls();
  public abstract void toNative(JBC.Met m);
  public abstract void toBQN(JBC.Met m);
  
  
  public static NType parse(String s) {
    if (s.length()==0) throw bad();
    if (s.equals("D")) return F64.INST;
    if (s.equals("I")) return I32.INST;
    if (s.equals("I*")) return I32P.INST;
    if (s.equals("D*")) return F64P.INST;
    throw bad();
  }
  
  private static SyntaxError bad() {
    return new SyntaxError("Bad BQNative function description");
  }
  
  static class I32 extends NType {
    static I32 INST = new I32();
    public Class<?> cls() { return Integer.TYPE; }
    public void toNative(JBC.Met m) {
      m.invvirt(Value.class, "asInt", JBC.met(Integer.TYPE));
    }
    public void toBQN(JBC.Met m) {
      m.invstat(Num.class, "of", JBC.met(Num.class, Integer.TYPE));
    }
  }
  static class I32P extends NType {
    static I32P INST = new I32P();
    public Class<?> cls() { return int[].class; }
    public void toNative(JBC.Met m) {
      m.invvirt(Value.class, "asIntArr", JBC.met(int[].class));
    }
    public void toBQN(JBC.Met m) {
      throw new DomainError("Cannot return a pointer");
    }
  }
  
  static class F64 extends NType {
    static F64 INST = new F64();
    public Class<?> cls() { return Double.TYPE; }
    public void toNative(JBC.Met m) {
      m.invvirt(Value.class, "asDouble", JBC.met(Double.TYPE));
    }
    public void toBQN(JBC.Met m) {
      m.invstat(Num.class, "of", JBC.met(Num.class, Double.TYPE));
    }
  }
  static class F64P extends NType {
    static F64P INST = new F64P();
    public Class<?> cls() { return double[].class; }
    public void toNative(JBC.Met m) {
      m.invvirt(Value.class, "asDoubleArr", JBC.met(double[].class));
    }
    public void toBQN(JBC.Met m) {
      throw new DomainError("Cannot return a pointer");
    }
  }
}
