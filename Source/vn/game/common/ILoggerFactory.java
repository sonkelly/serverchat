package vn.game.common;

import org.slf4j.Logger;

public abstract interface ILoggerFactory
{
  @SuppressWarnings("rawtypes")
public abstract Logger getLogger(Class paramClass);
}