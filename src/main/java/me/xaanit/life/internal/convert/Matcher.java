package me.xaanit.life.internal.convert;

import me.xaanit.life.internal.entities.token.Token;

public interface Matcher<T> {

  /**
   * Matches the token.
   *
   * @return Possibly-null {@link Token}
   */
  Token<T> convert();
}
