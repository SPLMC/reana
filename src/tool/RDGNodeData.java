package tool;

import java.util.Collection;

import fdtmc.FDTMC;

public class RDGNodeData {
	public String id;
	public FDTMC fdtmc;
	/**
	 * The node must have an associated presence condition, which is
	 * a boolean expression over features.
	 */
	public String presenceCondition;
	public Collection<RDGNode> dependencies;
	/**
	 * Height of the RDGNode.
	 */
	public int height;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FDTMC getFdtmc() {
		return fdtmc;
	}

	public void setFdtmc(FDTMC fdtmc) {
		this.fdtmc = fdtmc;
	}

	public String getPresenceCondition() {
		return presenceCondition;
	}

	public void setPresenceCondition(String presenceCondition) {
		this.presenceCondition = presenceCondition;
	}

	public Collection<RDGNode> getDependencies() {
		return dependencies;
	}

	public void setDependencies(Collection<RDGNode> dependencies) {
		this.dependencies = dependencies;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public RDGNodeData() {
	}
}