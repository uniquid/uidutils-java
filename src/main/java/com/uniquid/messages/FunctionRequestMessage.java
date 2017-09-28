package com.uniquid.messages;

import java.util.Objects;

/**
 * Represent a Function Request message: the User asks the provider to perform the specified function.
 */
public class FunctionRequestMessage implements UniquidMessage {

	private long id;

	private String user = "", parameters = "";

	private int function;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public int getFunction() {
		return function;
	}

	public void setFunction(int method) {
		this.function = method;
	}

	@Override
	public MessageType getMessageType() {

		return MessageType.FUNCTION_REQUEST;

	}

	@Override
	public boolean equals(Object object) {

		if (!(object instanceof FunctionRequestMessage))
			return false;

		if (this == object)
			return true;

		FunctionRequestMessage functionRequestMessage = (FunctionRequestMessage) object;

		return Objects.equals(id, functionRequestMessage.id) && Objects.equals(user, functionRequestMessage.user)
				&& Objects.equals(parameters, functionRequestMessage.parameters)
				&& Objects.equals(function, functionRequestMessage.function);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id, user, parameters, function);

	}

}
