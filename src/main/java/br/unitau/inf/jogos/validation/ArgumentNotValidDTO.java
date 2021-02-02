package br.unitau.inf.jogos.validation;

public class ArgumentNotValidDTO {
	private String field;
	private String errorMsg;

	public ArgumentNotValidDTO(String field, String errorMsg) {
            this.field = field;
            this.errorMsg = errorMsg;
    }

	public String getField() {
		return field;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
}