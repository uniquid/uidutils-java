/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.messages;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Represent a Capability message
 */
public class CapabilityMessage implements UniquidMessage {

    private String assigner; // Authority (owner) Address

    private String resourceID; // serviceProviderAddress
    private String assignee; // serviceUserAddress
    private String rights;
    private long since;
    private long until;

    private String assignerSignature;

    public CapabilityMessage() {

    }

    public String getAssigner() {
        return assigner;
    }

    public void setAssigner(String assigner) {
        this.assigner = assigner;
    }

    public String getResourceID() {
        return resourceID;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public long getSince() {
        return since;
    }

    public void setSince(long since) {
        this.since = since;
    }

    public long getUntil() {
        return until;
    }

    public void setUntil(long until) {
        this.until = until;
    }

    public String getAssignerSignature() {
        return assignerSignature;
    }

    public void setAssignerSignature(String assignerSignature) {
        this.assignerSignature = assignerSignature;
    }

    @Override
    public MessageType getMessageType() {

        return MessageType.UNIQUID_CAPABILITY;

    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof CapabilityMessage))
            return false;

        if (this == object)
            return true;

        CapabilityMessage capability = (CapabilityMessage) object;

        return Objects.equals(assigner, capability.assigner)
                && Objects.equals(resourceID, capability.resourceID)
                && Objects.equals(assignee, capability.assignee)
                && Objects.equals(rights, capability.rights)
                && Objects.equals(since, capability.since)
                && Objects.equals(until, capability.until);
    }

    @Override
    public int hashCode() {

        return Objects.hash(assigner, resourceID, assignee, rights, since, until);

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("assigner", assigner)
                .add("resourceID", resourceID)
                .add("assignee", assignee)
                .add("rights", rights)
                .add("since", since)
                .add("until", until)
                .add("assignerSignature", assignerSignature)
                .toString();
    }
}
