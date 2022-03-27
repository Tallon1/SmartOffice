// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: LightService.proto

package grpc.services.light;

/**
 * Protobuf type {@code Light.IntensityResponse}
 */
public final class IntensityResponse extends com.google.protobuf.GeneratedMessageV3 implements
		// @@protoc_insertion_point(message_implements:Light.IntensityResponse)
		IntensityResponseOrBuilder {
	private static final long serialVersionUID = 0L;

	// Use IntensityResponse.newBuilder() to construct.
	private IntensityResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
		super(builder);
	}

	private IntensityResponse() {
		intensity_ = 0;
	}

	@java.lang.Override
	public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
		return this.unknownFields;
	}

	private IntensityResponse(com.google.protobuf.CodedInputStream input,
			com.google.protobuf.ExtensionRegistryLite extensionRegistry)
			throws com.google.protobuf.InvalidProtocolBufferException {
		this();
		if (extensionRegistry == null) {
			throw new java.lang.NullPointerException();
		}
		int mutable_bitField0_ = 0;
		com.google.protobuf.UnknownFieldSet.Builder unknownFields = com.google.protobuf.UnknownFieldSet.newBuilder();
		try {
			boolean done = false;
			while (!done) {
				int tag = input.readTag();
				switch (tag) {
				case 0:
					done = true;
					break;
				case 8: {

					intensity_ = input.readInt32();
					break;
				}
				default: {
					if (!parseUnknownFieldProto3(input, unknownFields, extensionRegistry, tag)) {
						done = true;
					}
					break;
				}
				}
			}
		} catch (com.google.protobuf.InvalidProtocolBufferException e) {
			throw e.setUnfinishedMessage(this);
		} catch (java.io.IOException e) {
			throw new com.google.protobuf.InvalidProtocolBufferException(e).setUnfinishedMessage(this);
		} finally {
			this.unknownFields = unknownFields.build();
			makeExtensionsImmutable();
		}
	}

	public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
		return grpc.services.light.LightServiceImpl.internal_static_Light_IntensityResponse_descriptor;
	}

	@java.lang.Override
	protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
		return grpc.services.light.LightServiceImpl.internal_static_Light_IntensityResponse_fieldAccessorTable
				.ensureFieldAccessorsInitialized(grpc.services.light.IntensityResponse.class,
						grpc.services.light.IntensityResponse.Builder.class);
	}

	public static final int INTENSITY_FIELD_NUMBER = 1;
	private int intensity_;

	/**
	 * <code>int32 intensity = 1;</code>
	 */
	public int getIntensity() {
		return intensity_;
	}

	private byte memoizedIsInitialized = -1;

	@java.lang.Override
	public final boolean isInitialized() {
		byte isInitialized = memoizedIsInitialized;
		if (isInitialized == 1)
			return true;
		if (isInitialized == 0)
			return false;

		memoizedIsInitialized = 1;
		return true;
	}

	@java.lang.Override
	public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
		if (intensity_ != 0) {
			output.writeInt32(1, intensity_);
		}
		unknownFields.writeTo(output);
	}

	@java.lang.Override
	public int getSerializedSize() {
		int size = memoizedSize;
		if (size != -1)
			return size;

		size = 0;
		if (intensity_ != 0) {
			size += com.google.protobuf.CodedOutputStream.computeInt32Size(1, intensity_);
		}
		size += unknownFields.getSerializedSize();
		memoizedSize = size;
		return size;
	}

	@java.lang.Override
	public boolean equals(final java.lang.Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof grpc.services.light.IntensityResponse)) {
			return super.equals(obj);
		}
		grpc.services.light.IntensityResponse other = (grpc.services.light.IntensityResponse) obj;

		boolean result = true;
		result = result && (getIntensity() == other.getIntensity());
		result = result && unknownFields.equals(other.unknownFields);
		return result;
	}

	@java.lang.Override
	public int hashCode() {
		if (memoizedHashCode != 0) {
			return memoizedHashCode;
		}
		int hash = 41;
		hash = (19 * hash) + getDescriptor().hashCode();
		hash = (37 * hash) + INTENSITY_FIELD_NUMBER;
		hash = (53 * hash) + getIntensity();
		hash = (29 * hash) + unknownFields.hashCode();
		memoizedHashCode = hash;
		return hash;
	}

	public static grpc.services.light.IntensityResponse parseFrom(java.nio.ByteBuffer data)
			throws com.google.protobuf.InvalidProtocolBufferException {
		return PARSER.parseFrom(data);
	}

	public static grpc.services.light.IntensityResponse parseFrom(java.nio.ByteBuffer data,
			com.google.protobuf.ExtensionRegistryLite extensionRegistry)
			throws com.google.protobuf.InvalidProtocolBufferException {
		return PARSER.parseFrom(data, extensionRegistry);
	}

	public static grpc.services.light.IntensityResponse parseFrom(com.google.protobuf.ByteString data)
			throws com.google.protobuf.InvalidProtocolBufferException {
		return PARSER.parseFrom(data);
	}

	public static grpc.services.light.IntensityResponse parseFrom(com.google.protobuf.ByteString data,
			com.google.protobuf.ExtensionRegistryLite extensionRegistry)
			throws com.google.protobuf.InvalidProtocolBufferException {
		return PARSER.parseFrom(data, extensionRegistry);
	}

	public static grpc.services.light.IntensityResponse parseFrom(byte[] data)
			throws com.google.protobuf.InvalidProtocolBufferException {
		return PARSER.parseFrom(data);
	}

	public static grpc.services.light.IntensityResponse parseFrom(byte[] data,
			com.google.protobuf.ExtensionRegistryLite extensionRegistry)
			throws com.google.protobuf.InvalidProtocolBufferException {
		return PARSER.parseFrom(data, extensionRegistry);
	}

	public static grpc.services.light.IntensityResponse parseFrom(java.io.InputStream input)
			throws java.io.IOException {
		return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
	}

	public static grpc.services.light.IntensityResponse parseFrom(java.io.InputStream input,
			com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
		return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
	}

	public static grpc.services.light.IntensityResponse parseDelimitedFrom(java.io.InputStream input)
			throws java.io.IOException {
		return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
	}

	public static grpc.services.light.IntensityResponse parseDelimitedFrom(java.io.InputStream input,
			com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
		return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
	}

	public static grpc.services.light.IntensityResponse parseFrom(com.google.protobuf.CodedInputStream input)
			throws java.io.IOException {
		return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
	}

	public static grpc.services.light.IntensityResponse parseFrom(com.google.protobuf.CodedInputStream input,
			com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
		return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
	}

	public Builder newBuilderForType() {
		return newBuilder();
	}

	public static Builder newBuilder() {
		return DEFAULT_INSTANCE.toBuilder();
	}

	public static Builder newBuilder(grpc.services.light.IntensityResponse prototype) {
		return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
	}

	public Builder toBuilder() {
		return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
	}

	@java.lang.Override
	protected Builder newBuilderForType(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
		Builder builder = new Builder(parent);
		return builder;
	}

	/**
	 * Protobuf type {@code Light.IntensityResponse}
	 */
	public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
			// @@protoc_insertion_point(builder_implements:Light.IntensityResponse)
			grpc.services.light.IntensityResponseOrBuilder {
		public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
			return grpc.services.light.LightServiceImpl.internal_static_Light_IntensityResponse_descriptor;
		}

		@java.lang.Override
		protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
			return grpc.services.light.LightServiceImpl.internal_static_Light_IntensityResponse_fieldAccessorTable
					.ensureFieldAccessorsInitialized(grpc.services.light.IntensityResponse.class,
							grpc.services.light.IntensityResponse.Builder.class);
		}

		// Construct using grpc.services.light.IntensityResponse.newBuilder()
		private Builder() {
			maybeForceBuilderInitialization();
		}

		private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
			super(parent);
			maybeForceBuilderInitialization();
		}

		private void maybeForceBuilderInitialization() {
			if (com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders) {
			}
		}

		@java.lang.Override
		public Builder clear() {
			super.clear();
			intensity_ = 0;

			return this;
		}

		@java.lang.Override
		public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
			return grpc.services.light.LightServiceImpl.internal_static_Light_IntensityResponse_descriptor;
		}

		public grpc.services.light.IntensityResponse getDefaultInstanceForType() {
			return grpc.services.light.IntensityResponse.getDefaultInstance();
		}

		public grpc.services.light.IntensityResponse build() {
			grpc.services.light.IntensityResponse result = buildPartial();
			if (!result.isInitialized()) {
				throw newUninitializedMessageException(result);
			}
			return result;
		}

		public grpc.services.light.IntensityResponse buildPartial() {
			grpc.services.light.IntensityResponse result = new grpc.services.light.IntensityResponse(this);
			result.intensity_ = intensity_;
			onBuilt();
			return result;
		}

		@java.lang.Override
		public Builder clone() {
			return (Builder) super.clone();
		}

		@java.lang.Override
		public Builder setField(com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
			return (Builder) super.setField(field, value);
		}

		@java.lang.Override
		public Builder clearField(com.google.protobuf.Descriptors.FieldDescriptor field) {
			return (Builder) super.clearField(field);
		}

		@java.lang.Override
		public Builder clearOneof(com.google.protobuf.Descriptors.OneofDescriptor oneof) {
			return (Builder) super.clearOneof(oneof);
		}

		@java.lang.Override
		public Builder setRepeatedField(com.google.protobuf.Descriptors.FieldDescriptor field, int index,
				java.lang.Object value) {
			return (Builder) super.setRepeatedField(field, index, value);
		}

		@java.lang.Override
		public Builder addRepeatedField(com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
			return (Builder) super.addRepeatedField(field, value);
		}

		@java.lang.Override
		public Builder mergeFrom(com.google.protobuf.Message other) {
			if (other instanceof grpc.services.light.IntensityResponse) {
				return mergeFrom((grpc.services.light.IntensityResponse) other);
			} else {
				super.mergeFrom(other);
				return this;
			}
		}

		public Builder mergeFrom(grpc.services.light.IntensityResponse other) {
			if (other == grpc.services.light.IntensityResponse.getDefaultInstance())
				return this;
			if (other.getIntensity() != 0) {
				setIntensity(other.getIntensity());
			}
			this.mergeUnknownFields(other.unknownFields);
			onChanged();
			return this;
		}

		@java.lang.Override
		public final boolean isInitialized() {
			return true;
		}

		@java.lang.Override
		public Builder mergeFrom(com.google.protobuf.CodedInputStream input,
				com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
			grpc.services.light.IntensityResponse parsedMessage = null;
			try {
				parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
			} catch (com.google.protobuf.InvalidProtocolBufferException e) {
				parsedMessage = (grpc.services.light.IntensityResponse) e.getUnfinishedMessage();
				throw e.unwrapIOException();
			} finally {
				if (parsedMessage != null) {
					mergeFrom(parsedMessage);
				}
			}
			return this;
		}

		private int intensity_;

		/**
		 * <code>int32 intensity = 1;</code>
		 */
		public int getIntensity() {
			return intensity_;
		}

		/**
		 * <code>int32 intensity = 1;</code>
		 */
		public Builder setIntensity(int value) {

			intensity_ = value;
			onChanged();
			return this;
		}

		/**
		 * <code>int32 intensity = 1;</code>
		 */
		public Builder clearIntensity() {

			intensity_ = 0;
			onChanged();
			return this;
		}

		@java.lang.Override
		public final Builder setUnknownFields(final com.google.protobuf.UnknownFieldSet unknownFields) {
			return super.setUnknownFieldsProto3(unknownFields);
		}

		@java.lang.Override
		public final Builder mergeUnknownFields(final com.google.protobuf.UnknownFieldSet unknownFields) {
			return super.mergeUnknownFields(unknownFields);
		}

		// @@protoc_insertion_point(builder_scope:Light.IntensityResponse)
	}

	// @@protoc_insertion_point(class_scope:Light.IntensityResponse)
	private static final grpc.services.light.IntensityResponse DEFAULT_INSTANCE;
	static {
		DEFAULT_INSTANCE = new grpc.services.light.IntensityResponse();
	}

	public static grpc.services.light.IntensityResponse getDefaultInstance() {
		return DEFAULT_INSTANCE;
	}

	private static final com.google.protobuf.Parser<IntensityResponse> PARSER = new com.google.protobuf.AbstractParser<IntensityResponse>() {
		public IntensityResponse parsePartialFrom(com.google.protobuf.CodedInputStream input,
				com.google.protobuf.ExtensionRegistryLite extensionRegistry)
				throws com.google.protobuf.InvalidProtocolBufferException {
			return new IntensityResponse(input, extensionRegistry);
		}
	};

	public static com.google.protobuf.Parser<IntensityResponse> parser() {
		return PARSER;
	}

	@java.lang.Override
	public com.google.protobuf.Parser<IntensityResponse> getParserForType() {
		return PARSER;
	}

	public grpc.services.light.IntensityResponse getDefaultInstanceForType() {
		return DEFAULT_INSTANCE;
	}

}
