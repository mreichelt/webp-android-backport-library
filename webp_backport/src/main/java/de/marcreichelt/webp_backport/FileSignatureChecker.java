package de.marcreichelt.webp_backport;

public class FileSignatureChecker {

    /**
     * Check if a given byte array has the necessary WebP signature in the beginning of its content.
     * The signature is 'RIFFxxxxWEBP' where 'xxxx' is the file size.
     *
     * @param image the byte array to check
     * @return true if the byte array has the WebP signature else false
     */
    public static boolean checkForWebP(byte[] image) {
        return checkForRIFF(image) // starts with 'RIFF'
                && byteArrayContainsAt(image, new byte[]{87, 69, 66, 80}, 8); // index 8 to 11 must be 'WEBP'
    }

    /**
     * Check if a given byte array has a signature that starts with 'RIFF'
     *
     * @param data the byte array to check
     * @return true if the byte array starts with RIFF else false
     */
    public static boolean checkForRIFF(byte[] data) {
        return byteArrayStartsWith(data, new byte[]{82, 73, 70, 70}); // starts with 'RIFF'
    }


    /**
     * Check if a byte array starts with the same bytes as another byte array
     * that is however smaller than the first one.
     *
     * @param array        e.g. {-1, -40, -1, -32, ........}
     * @param startingWith e.g. {-1, -40, -1, -32}
     * @return true if {@code array} starts with the same bytes as {@code startingWith}
     */
    public static boolean byteArrayStartsWith(byte[] array, byte[] startingWith) {
        return byteArrayContainsAt(array, startingWith, 0);
    }


    /**
     * Check if a byte array contains another byte array that is equal or smaller in size
     *
     * @param haystack e.g. {..., -1, -40, -1, -32, ...}
     * @param needle   e.g. {-1, -40, -1, -32}
     * @param offset   the index where {@code needle} should be starting in {@code haystack}
     * @return true if {@code needle} is contained in {@code haystack} starting at index {@code offset}
     */
    public static boolean byteArrayContainsAt(byte[] haystack, byte[] needle, int offset) {
        if (needle.length < 1) {
            return true; // everything starts with empty
        }
        if (haystack.length < 1 /* haystack is empty so needle is not contained */
                || (haystack.length < needle.length) /* haystack can not contain a needle that is bigger than the haystack */
                || (offset < 0 || offset >= haystack.length) /* offset must be between 0 and last index of haystack */
                || (haystack.length < needle.length + offset) /* haystack size must not be smaller than the offset combined
                with the needle length, otherwise index out of array */) {
            return false;
        }

        for (int i = 0; i < needle.length; i++) {
            if (needle[i] != haystack[offset + i]) {
                return false;
            }
        }
        return true;
    }


}