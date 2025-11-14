package src.main.resources.script

public class AttributeDescriptor {

    private String qualifier;
    private IntegrationType enclosingType;

    public AttributeDescriptor() {

    }

    public AttributeDescriptor(String qualifier, IntegrationType enclosingType) {
        this.setQualifier(qualifier);
        this.setEnclosingType(enclosingType);
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public IntegrationType getEnclosingType() {
        return enclosingType;
    }

    public void setEnclosingType(IntegrationType enclosingType) {
        this.enclosingType = enclosingType;
    }

    public static class Builder
    {
        private String qualifier;
        private IntegrationType enclosingType;
        public Builder withQualifier(String qualifier)
        {
            this.qualifier=qualifier;
            return this;
        }

        public Builder withIntegrationType(IntegrationType enclosingType)
        {
            this.enclosingType=enclosingType;
            return this;
        }

        public AttributeDescriptor build()
        {
            return new AttributeDescriptor(qualifier,enclosingType);
        }

    }


}