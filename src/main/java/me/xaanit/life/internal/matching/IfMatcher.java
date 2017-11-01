package me.xaanit.life.internal.matching;

import me.xaanit.life.internal.entities.IfStatement;
import me.xaanit.life.internal.entities.token.Token;
import me.xaanit.life.internal.entities.token.Tokeniser;
import me.xaanit.life.internal.exceptions.TokeniserException;

public class IfMatcher extends Tokenisable implements Matchable<IfStatement> {

  public IfMatcher(final Tokeniser tokeniser) {
    super(tokeniser);
  }

  @Override
  public Token<IfStatement> convert(String input, Token parent) {
    Token<IfStatement> token;
    if (parent == null) {
      throw new TokeniserException("If statements much be contained within methods!");
    }

//    IfStatement statement = new IfStatement()
    return null;
  }
}
