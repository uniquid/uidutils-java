package com.uniquid.messages;

import java.util.Objects;

/**
 * Represents an Announce Message: a message sent by a Node when it appear the first time.
 */
public class AnnounceMessage implements UniquidMessage {

	private String name = "", pubKey = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPubKey() {
		return pubKey;
	}

	public void setPubKey(String pubKey) {
		this.pubKey = pubKey;
	}

	@Override
	public MessageType getMessageType() {

		return MessageType.ANNOUNCE;

	}

	@Override
	public boolean equals(Object object) {

		if (!(object instanceof AnnounceMessage))
			return false;

		if (this == object)
			return true;

		AnnounceMessage announceMessage = (AnnounceMessage) object;

		return Objects.equals(name, announceMessage.name) && Objects.equals(pubKey, announceMessage.pubKey);

	}

	@Override
	public int hashCode() {

		return Objects.hash(name, pubKey);

	}


}
