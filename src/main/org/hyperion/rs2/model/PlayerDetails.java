package org.hyperion.rs2.model;

import org.apache.mina.core.session.IoSession;
import org.hyperion.rs2.net.ISAACCipher;
import org.hyperion.rs2.net.security.EncryptionStandard;

public final class PlayerDetails {

	private IoSession session;
	private final String name, password, IpAddress;
	private final int macAddress, UID, authenticationCode;
	private final ISAACCipher inCipher, outCipher;
	private final int[] specialUid;
	private final boolean saveIp;

	public PlayerDetails(IoSession session, String name, String password, int authenticationCode, int macAddress, int UID, ISAACCipher inCipher, ISAACCipher outCipher, String IpAddress, int[] specialUid, boolean saveIp) {
		this.session = session;
		this.name = name;
		this.password = EncryptionStandard.encryptPassword(password);
		this.macAddress = macAddress;
		this.inCipher = inCipher;
		this.outCipher = outCipher;
		this.IpAddress = IpAddress;
		this.specialUid = specialUid;
		this.UID = UID;
		this.authenticationCode = authenticationCode;
		this.saveIp = saveIp;
	}

	public IoSession getSession() {
		return session;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public int getMacAddress() {
		return macAddress;
	}
	public ISAACCipher getInCipher() {
		return inCipher;
	}
	public ISAACCipher getOutCipher() {
		return outCipher;
	}
	public String getIpAddress() {
		return IpAddress;
	}
	public int[] getSpecialUid() {
		return specialUid;
	}
	public int getUID() {
		return UID;
	}
	public int getAuthenticationCode() {
		return authenticationCode;
	}
	public boolean saveIp() {
		return saveIp;
	}
}