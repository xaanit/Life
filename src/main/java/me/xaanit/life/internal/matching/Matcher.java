package me.xaanit.life.internal.matching;

import me.xaanit.life.internal.entities.token.Token;

public interface Matcher<T> {

  /**
   * Matches the token.
   *
   * @return Possibly-null
   */
  Token<T> matchToken();
}
