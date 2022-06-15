package frc.team4481.frclibrary4481.throwable;

public class HardwareException extends Exception {
    /**
     * This exception is thrown when there are known errors in the hardware that needs to be documented in the code
     */
    private String mCode;

    /**
     *
     * @param pCode recognizable exception code
     * @param pMessage detailed description
     */
    public HardwareException(String pCode, String pMessage){
        super(pMessage);
        this.setCode(pCode);
    }

    /**
     *
     * @param pCode recognizable exception code
     * @param pMessage detailed description
     * @param pCause when the exception is thrown by another exception
     */
    public HardwareException(String pCode, String pMessage, Throwable pCause) {
        super(pMessage, pCause);
        this.setCode(pCode);
    }

    /**
     *
     * @return recognizable exception code
     */
    public String getCode() {
        return mCode;
    }

    /**
     *
     * @param pCode recognizable exception code
     */
    public void setCode(String pCode) {
        mCode = pCode;
    }
}
