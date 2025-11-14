    @FunctionLibraryMethod(category = "UDFFormatPool", title = "removeSpaces", executionType = "SINGLE_VALUE", key = "calculate1")
    public String removeSpaces(@UDFParam(paramCategory = "Argument", title = "Input Value") String inputValue, Container container) throws StreamTransformationException {
        String escapedValue = null;
        if (inputValue != null) {
            escapedValue = inputValue.replaceAll(" ", "");
        }
        return escapedValue;
    }