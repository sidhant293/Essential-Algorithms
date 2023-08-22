class KMP {
  boolean kmp(String str1, String str2) {
    // str2 needs to be searched in str1
        int n = str1.length(), m = str2.length();
        int lps[] = new int[m];
        int j = 1, prev = 0, i = 0;

        while(j < m) {
            if(str2.charAt(j) == str2.charAt(prev)) {
                prev++;
                lps[j] = prev;
                j++;
            } else {
                if(prev == 0) {
                    lps[j] = 0;
                    j++;
                } else {
                    prev = lps[prev - 1];
                }
            }
        }

        j = 0;

        while(i < n) {
            if(str1.charAt(i) == str2.charAt(j)) {
                i++; j++;
                if(j == m) return true;
            } else {
                if(j == 0) i++;
                else j = lps[j - 1];
            }
        }
        return false;
    }
}
