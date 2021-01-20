package vn.game.session;

//import com.punch.framework.common.ILoggerFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;

public class IdGenerator
{
  private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(IdGenerator.class);
  @SuppressWarnings("unused")
private final int DEFAULT_ID_LENGTH = 32;
  private final AtomicLong mNextUid;
  @SuppressWarnings("unused")
private static final long INIT_UID = -1000L;
  @SuppressWarnings("unused")
private static final long MIN_ID = 1000L;
  protected static final String DEFAULT_ALGORITHM = "MD5";
  private String algorithm = "MD5";
  private Random random = new SecureRandom();
  private MessageDigest digest = null;

  public IdGenerator()
  {
    long seed = System.currentTimeMillis();

    char[] entropy = toString().toCharArray();
    for (int i = 0; i < entropy.length; ++i)
    {
      long update = (byte)entropy[i] << i % 8 * 8;
      seed ^= update;
    }

    this.random.setSeed(seed);

    this.mNextUid = new AtomicLong(-1000L);
  }

  public synchronized long generateUID()
  {
    synchronized (this.mNextUid)
    {
      long result = this.mNextUid.getAndDecrement();
      if (result <= 1000L)
      {
        result = result - 1000L + -1000L;
        this.mNextUid.set(result - 1L);
      }
      return result;
    }
  }

  public synchronized String generateId()
  {
    int length = 32;
    byte[] buffer = new byte[length];

    StringBuffer reply = new StringBuffer();

    int resultLenBytes = 0;
    while (resultLenBytes < length)
    {
      this.random.nextBytes(buffer);
      buffer = getDigest().digest(buffer);

      for (int j = 0; (j < buffer.length) && (resultLenBytes < length); ++j)
      {
        byte b1 = (byte)((buffer[j] & 0xF0) >> 4);
        if (b1 < 10)
        {
          reply.append((char)(48 + b1));
        }
        else {
          reply.append((char)(65 + b1 - 10));
        }

        byte b2 = (byte)(buffer[j] & 0xF);
        if (b2 < 10)
        {
          reply.append((char)(48 + b2));
        }
        else {
          reply.append((char)(65 + b2 - 10));
        }

        ++resultLenBytes;
      }
    }

    return reply.toString();
  }

  public synchronized String getAlgorithm()
  {
    return this.algorithm;
  }

  public synchronized void setAlgorithm(String algorithm)
  {
    this.algorithm = algorithm;
    this.digest = null;
  }

  private MessageDigest getDigest()
  {
    if (this.digest == null)
    {
      try
      {
        this.digest = MessageDigest.getInstance(this.algorithm);
        if(this.digest == null) this.mLog.debug(" MessageDigest IS NULL ");
        
      }
      catch (NoSuchAlgorithmException ex)
      {
        try {
          this.digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException ex2) {
          this.digest = null;
          throw new IllegalStateException("No algorithms for IdGenerator");
        }
      }

      this.mLog.debug("Using MessageDigest: " + this.digest.getAlgorithm());
    }

    return this.digest;
  }

  public final String toString()
  {
    return super.toString();
  }
  
  
  public static void main(String args[]) {
	  System.out.println("Test data");
	  
	  IdGenerator generator = new IdGenerator();
	  
	  for (int i =1; i< 5000; i++) {
		  System.out.println("Generate ID " + generator.generateId());
		  System.out.println("Generate UID " + generator.generateUID());
	  }
	  
  }
  
  
}