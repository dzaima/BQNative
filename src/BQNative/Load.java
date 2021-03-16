package BQNative;

import BQN.errors.DomainError;
import BQN.tools.FmtInfo;
import BQN.types.*;

public class Load extends Fun {
  public String ln(FmtInfo fmtInfo) { return "BQNative.Load"; }
  
  public Value call(Value w, Value x) {
    String file = w.asString();
    String desc = x.asString();
    String[] ps = desc.split(" ");
    if (ps.length<2) throw new DomainError("BQNative.Load: 𝕩 must have at least 2 space-separated parts");
    NType ret = NType.parse(ps[0]);
    NType[] args = new NType[ps.length-2];
    for (int i = 0; i < args.length; i++) args[i] = NType.parse(ps[i+2]);
    return new NativeGen(ret, args, file+"::"+desc, file, ps[1]).f;
  }
}
