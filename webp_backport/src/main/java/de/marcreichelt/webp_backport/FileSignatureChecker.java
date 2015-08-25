package de.marcreichelt.webp_backport;

class FileSignatureChecker {

    /**
     * Check if a given byte array has the necessary WebP signature in the beginning of its content
     *
     * @param imgByteArr the byte array to check
     * @return true if the byte array has the WebP signature else false
     */
    static boolean checkForWebP(byte[] imgByteArr) {
        if (byteArrayStartsWith(imgByteArr, new byte[]{82, 73, 70, 70})) { // bytes 1 to 3 must be 'RIFF'

            // bytes 4 to 7 == file length information, this info differs every time so don't check

            // bytes 8 to 11 must be 'WEBP'
            if (imgByteArr.length >= 12) {
                byte[] webp = new byte[]{87, 69, 66, 80}; // 'WEBP'
                for (int i = 8; i <= 11; i++) {
                    if (imgByteArr[i] != webp[i - 8]) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }


    /**
     * Check if a byte array starts with the same bytes as another byte array that is smaller
     * however smaller than the first one.
     *
     * @param byteArrayToCheck e.g. {-1, -40, -1, -32, ........}
     * @param signatureInBytes e.g. {-1, -40, -1, -32}
     * @return true if byteArrayToCheck starts with the same bytes as byteArrayToCheck
     */
    private static boolean byteArrayStartsWith(byte[] byteArrayToCheck, byte[] signatureInBytes) {
        if (byteArrayToCheck == null || byteArrayToCheck.length < 1) {
            return false;
        }
        if (signatureInBytes == null || signatureInBytes.length < 1) {
            return true; // everything starts with empty
        }
        if (byteArrayToCheck.length < signatureInBytes.length) {
            return false; // can not start with something that is longer
        }

        for (int i = 0; i < signatureInBytes.length; i++) {
            if (signatureInBytes[i] != byteArrayToCheck[i]) {
                return false;
            }
        }
        return true;
    }


}