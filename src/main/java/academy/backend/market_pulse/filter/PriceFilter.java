package academy.backend.market_pulse.filter;

import academy.backend.market_pulse.model.Instrument;
import academy.backend.market_pulse.model.Stock;

import java.math.BigDecimal;

/**
 * Отбор по цене — применим только к {@link Stock}: остальные типы инструментов сегодня не несут
 * числового значения цены, поэтому для них {@code matches} всегда возвращает {@code false}. В
 * роли цены — дивидендная доходность ({@link Stock#getDividendYield()}), единственный числовой
 * атрибут акции на этом этапе проекта.
 */

public class PriceFilter implements InstrumentFilter {
    public enum Operator {
        GE {
            @Override
            public boolean apply(BigDecimal DividendYield, BigDecimal threshold) {
                return DividendYield.compareTo(threshold) >= 0;
            }
        },
        LE {
            @Override
            public boolean apply(BigDecimal DividendYield, BigDecimal threshold) {
                return DividendYield.compareTo(threshold) <= 0;
            }
        },
        EQ {
            @Override
            public boolean apply(BigDecimal DividendYield, BigDecimal threshold) {
                return DividendYield.compareTo(threshold) == 0;
            }
        };

        public abstract boolean apply(BigDecimal DividendYield, BigDecimal threshold);
    }
    private final Operator operator;
    private final BigDecimal threshold;

    public PriceFilter(Operator operator, BigDecimal threshold) {
        this.operator = operator;
        this.threshold = threshold;
    }

    @Override
    public boolean matches(Instrument instrument) {
        if (instrument instanceof Stock stock) {
            return operator.apply(stock.getDividendYield(), threshold);
        }
        return false;
    }
}