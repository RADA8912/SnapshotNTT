package src.main.resources.script

public class IntegrationType implements Serializable{

    private String code;
    private String integrationKey;

    public IntegrationType()
    {

    }

    public IntegrationType(String code, String integrationKey)
    {
        this.setCode(code);
        this.setIntegrationKey(integrationKey);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIntegrationKey() {
        return integrationKey;
    }

    public void setIntegrationKey(String integrationKey) {
        this.integrationKey = integrationKey;
    }


   public static class Builder
    {
        private String code;
        private String integrationKey;
        public Builder()
        {

        }

        public Builder withCode(String code)
        {
            this.code=code;
            return this;
        }

        public Builder withIntegrationKey(String integrationKey)
        {
            this.integrationKey=integrationKey;
            return this;
        }

        public IntegrationType build()
        {
            return new IntegrationType(this.code,this.integrationKey);
        }
    }
}