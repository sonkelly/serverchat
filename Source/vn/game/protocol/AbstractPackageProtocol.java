package vn.game.protocol;

import java.util.Hashtable;

import vn.game.bytebuffer.IByteBuffer;
import vn.game.common.ServerException;
import vn.game.session.ISession;

public abstract class AbstractPackageProtocol implements IPackageProtocol {
	private final Hashtable<Integer, IMessageProtocol> mMessages;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AbstractPackageProtocol() {
		this.mMessages = new Hashtable();
	}

	public IMessageProtocol getMessageProtocol(int aMsgId) {
		IMessageProtocol msgProtocol = (IMessageProtocol) this.mMessages.get(Integer.valueOf(aMsgId));
		return msgProtocol;
	}

	public IRequestPackage decodeNew(ISession paramISession, IByteBuffer paramIByteBuffer) throws ServerException {
		return null;
	}

	void addMessageProtocol(int aMsgId, IMessageProtocol msgData) {
		if (msgData != null) {
			this.mMessages.put(Integer.valueOf(aMsgId), msgData);
		}
	}
}