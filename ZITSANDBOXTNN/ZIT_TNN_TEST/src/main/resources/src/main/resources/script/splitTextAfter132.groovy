import com.sap.aii.mapping.api.*; 
import com.sap.it.api.mapping.*;

public void splitTextafter132(String[] a, Output result) {
//UDF to Split Text at every 132 characters
    int n = 0;
    int p = 0;
    for (int i = 0; i < a.length; i++) {
        if (a[i].length() < 133)
            result.addValue(a[i]);

        else {

            int c = a[i].length() / 132;
            int r = a[i].length() % 132;
            if (r == 0) {
                n = c;
            } else {
                n = c + 1;
            }
            for (int j = 0; j < n; j++) {
                if (p < (a[i].length() - 132)) {
                    result.addValue(a[i].substring(p, p + 132));
                    p = p + 132;
                } else
                    result.addValue(a[i].substring(p, a[i].length()));

            }
        }

    }
}