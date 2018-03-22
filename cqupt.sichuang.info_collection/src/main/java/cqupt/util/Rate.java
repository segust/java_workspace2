package cqupt.util;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author
 * @date 2017年8月22日 下午8:59:39
 * @parameter
 */
public class Rate {
    public static BigDecimal getRate(BigDecimal beginlat, BigDecimal beginlng, BigDecimal endlat, BigDecimal endlng,
                                     Timestamp begintime, Timestamp endtime) {
        BigDecimal rate = (beginlat.subtract(endlat).pow(2)).add(beginlng.subtract(endlng).pow(2)).divide(new BigDecimal(endtime.getTime() - begintime.getTime()).abs(), 3);
        return rate;
    }

}
