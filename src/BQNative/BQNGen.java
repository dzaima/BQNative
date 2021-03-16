package BQNative;

import BQN.errors.ImplementationError;
import BQN.tools.*;
import BQN.types.Value;
import com.sun.jna.Library;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BQNGen extends JBC {
  public final NativeFn f;
  static AtomicInteger NAME = new AtomicInteger(0);
  public BQNGen(NType ret, NType[] args, Class<Library> lib, String desc, String sym, String metType) {
    String this_name = "BQNative/fn"+NAME.incrementAndGet();
    
    {
      Met fn = new Met(0x1001, "<init>", "()V", 1);
      fn.aload(0);
      fn.invspec(NativeFn.class, "<init>", "()V");
      fn.vret();
      fn.mstack = 1;
      methods.add(fn);
    }
    
    {
      Met fn = new Met(0x0001, "call", met(Value.class, Value.class), 2);
      int THIS = 0;
      int ARG = 1;
      fn.aload(THIS);
      for (int i = 0; i < args.length; i++) {
        fn.aload(ARG);
        fn.iconst(i);
        fn.invvirt(Value.class, "get", met(Value.class, Integer.TYPE));
        args[i].toNative(fn);
      }
      fn.invstat(lib, sym, metType);
      ret.toBQN(fn);
      fn.aret();
      fn.mstack = 10+args.length;
      methods.add(fn);
    }
    
    {
      Met fn = new Met(0x0001, "ln", met(String.class, FmtInfo.class), 2);
      fn.ldc(desc);
      fn.aret();
      fn.mstack = 1;
      methods.add(fn);
    }
  
    byte[] bc = finish(CONSTANT_Class(this_name), CONSTANT_Class(NativeFn.class));
  
    if (bc==null) {
      f = null;
      return;
    }
    // try { Files.write(Paths.get("fn.class"), bc); } catch (IOException e) { e.printStackTrace(); }
    Class<?> def = l.def(null, bc, 0, bc.length);
    try {
      f = (NativeFn) def.getDeclaredConstructor().newInstance();
    } catch (Throwable e) {
      throw new ImplementationError(e);
    }
  }
}
