package br.com.alepmendonca.model;

public class Store {

	private long id;
	private String originalName;
	private String userDefinedName;
	private StoreType type;

	public Store(long _id, String _originalName, String _userDefinedName,
			StoreType _type) {
		super();
		setId(_id);
		setOriginalName(_originalName);
		setUserDefinedName(_userDefinedName);
		setType(_type);
	}

	public long getId() {
		return id;
	}

	private void setId(long id) {
		this.id = id;
	}

	public String getOriginalName() {
		return originalName;
	}

	private void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getUserDefinedName() {
		return userDefinedName;
	}

	private void setUserDefinedName(String userDefinedName) {
		this.userDefinedName = userDefinedName;
	}

	public StoreType getType() {
		return type;
	}

	private void setType(StoreType type) {
		this.type = type;
	}

	public static class StoreType {
		private long id;
		private String typeName;
		private StoreType parentType;
		
		public StoreType(long _id, String _typeName, StoreType _parent) {
			super();
			setId(_id);
			setTypeName(_typeName);
			setParentType(_parent);
		}

		public long getId() {
			return id;
		}
		private void setId(long id) {
			this.id = id;
		}
		public String getTypeName() {
			return typeName;
		}
		private void setTypeName(String typeName) {
			this.typeName = typeName;
		}
		public StoreType getParentType() {
			return parentType;
		}
		private void setParentType(StoreType type) {
			this.parentType = type;
		}
	}
}
