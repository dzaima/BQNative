package BQNative;

import BQN.errors.ImplementationError;
import BQN.tools.*;
import com.sun.jna.*;

import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unchecked")
public class NativeGen extends JBC {
  public NativeFn f;
  static AtomicInteger NAME = new AtomicInteger(0);
  public NativeGen(NType ret, NType[] args, String desc, String path, String sym) {
    access_flags = 0x0001; // ACC_PUBLIC
    interfaces.add(CONSTANT_Class(Library.class));
    String metType;
    {
      Class<?>[] ac = new Class[args.length];
      for (int i = 0; i < args.length; i++) ac[i] = args[i].cls();
      metType = met(ret.cls(), ac);
      Met fn = new Met(0x0109, sym, metType, 2); // ACC_NATIVE ACC_STATIC ACC_PUBLIC
      methods.add(fn);
    }
  
    int this_class = CONSTANT_Class("BQNative/lib"+NAME.incrementAndGet());
    byte[] bc = finish(this_class, CONSTANT_Class(Object.class));
    if (bc==null) {
      f = null;
      return;
    }
    Class<Library> def = (Class<Library>) l.def(null, bc, 0, bc.length);
    try {
      Native.register(def, path);
      f = new BQNGen(ret, args, def, desc, sym, metType).f;
    } catch (Throwable e) {
      throw new ImplementationError(e);
    }
  }
}
