package cz.mg.crisp.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.crisp.entity.Line;
import cz.mg.crisp.entity.LocalPoint;
import cz.mg.crisp.entity.LocalVector;

public @Service class LineCollisionService {
    private static @Optional LineCollisionService instance;

    public static @Mandatory LineCollisionService getInstance() {
        if (instance == null) {
            instance = new LineCollisionService();
        }
        return instance;
    }

    private LineCollisionService() {
    }

    /**
     * V = B - A
     * A + V * t = P
     * t € <0;1>
     * -------------
     * a1 + v1 * u = a2 + v2 * v
     * -------------------------
     * a1x + v1x * t1 = a2x + v2x * t2
     * a1y + v1y * t1 = a2y + v2y * t2
     * -------------------------------
     * a + iu = e + kv
     * b + ju = f + lv
     * ---------------
     * looking for u and v
     * -------------------
     * a + iu = e + kv
     * iu = e + kv - a
     * u = (e + kv - a) / i
     * --------------------
     * b + ju = f + lv                        || u = (e + kv - i) / i
     * b + j * ((e + kv - a) / i) = f + lv    || * i
     * bi + j * (e + kv - a) = fi + lvi       || ()
     * bi + je + jkv - ja = fi + lvi          || <- v
     * jkv - lvi = fi - bi - je + ja          || ()
     * v * (jk - li) = fi - bi - je + ja      || / (jk - li)
     * v = (fi - bi - je + ja) / (jk - li)
     * --------------------------------------------------------------
     * a + iu = e + kv
     * kv = a + iu - e
     * v = (a + iu - e) / k
     * --------------------
     * b + ju = f + lv                        || v = (a + iu - e) / k
     * b + ju = f + l * ((a + iu - e) / k)    || * k
     * bk + juk = fk + l * (a + iu - e)       || ()
     * bk + juk = fk + la + liu - le          || <- u
     * juk - liu = fk + la - le - bk          || ()
     * u * (jk - li) = fk + la - le - bk      || / (jk - li)
     * u = (fk + la - le - bk) / (jk - li)
     */
    public boolean collision(@Mandatory Line l1, @Mandatory Line l2) {
        LocalPoint a1 = l1.getBegin();
        LocalPoint b1 = l1.getEnd();
        LocalPoint a2 = l2.getBegin();
        LocalPoint b2 = l2.getEnd();
        LocalVector v1 = LocalPoint.minus(b1, a1);
        LocalVector v2 = LocalPoint.minus(b2, a2);

        double a = a1.getX();
        double b = a1.getY();
        double e = a2.getX();
        double f = a2.getY();
        double i = v1.getX();
        double j = v1.getY();
        double k = v2.getX();
        double l = v2.getY();

        double vTop = f*i - b*i - j*e + j*a;
        double vBot = j*k - l*i;
        double uTop = f*k + l*a - l*e - b*k;
        double uBot = j*k - l*i;

        if (vBot != 0 && uBot != 0) {
            double v = vTop / vBot;
            double u = uTop / uBot;
            return v >= 0 && v <= 1 && u >= 0 && u <= 1;
        } else {
            return false;
        }
    }
}
