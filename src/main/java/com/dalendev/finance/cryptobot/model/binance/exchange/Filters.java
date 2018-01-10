package com.dalendev.finance.cryptobot.model.binance.exchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.SimpleType;

import java.io.IOException;

/**
 * @author daniele.orler
 */
public class Filters {

    public enum FilterType {
        PRICE_FILTER,
        LOT_SIZE,
        MIN_NOTIONAL
    }

    @JsonTypeInfo(use = Id.CUSTOM, property = "filterType", include = As.EXISTING_PROPERTY)
    @JsonTypeIdResolver(value = Resolver.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static abstract class Filter {

        private FilterType filterType;

        public FilterType getFilterType() {
            return filterType;
        }

        public void setFilterType(FilterType filterType) {
            this.filterType = filterType;
        }
    }

    public static class PriceFilter extends Filter {

        private Float minPrice;
        private Float maxPrice;
        private Float tickSize;

        public Float getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(Float minPrice) {
            this.minPrice = minPrice;
        }

        public Float getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(Float maxPrice) {
            this.maxPrice = maxPrice;
        }

        public Float getTickSize() {
            return tickSize;
        }

        public void setTickSize(Float tickSize) {
            this.tickSize = tickSize;
        }
    }

    public static class LotFilter extends Filter {

        @JsonProperty("minQty")
        private Float minQuantity;
        @JsonProperty("maxQty")
        private Float maxQuantity;
        private Float stepSize;

        public Float getMinQuantity() {
            return minQuantity;
        }

        public void setMinQuantity(Float minQuantity) {
            this.minQuantity = minQuantity;
        }

        public Float getMaxQuantity() {
            return maxQuantity;
        }

        public void setMaxQuantity(Float maxQuantity) {
            this.maxQuantity = maxQuantity;
        }

        public Float getStepSize() {
            return stepSize;
        }

        public void setStepSize(Float stepSize) {
            this.stepSize = stepSize;
        }
    }

    public static class NotionalFilter extends Filter {

        private Float minNotional;

        public Float getMinNotional() {
            return minNotional;
        }

        public void setMinNotional(Float minNotional) {
            this.minNotional = minNotional;
        }
    }


    private static class Resolver extends TypeIdResolverBase {

        @Override
        public String idFromValue(Object o) {
            return null;
        }

        @Override
        public String idFromValueAndType(Object o, Class<?> aClass) {
            return null;
        }

        @Override
        public Id getMechanism() {
            return Id.CUSTOM;
        }

        @Override
        public JavaType typeFromId(DatabindContext context, String id) throws IOException {

            FilterType filterType = FilterType.valueOf(id);

            switch (filterType) {
                case LOT_SIZE:
                    return SimpleType.constructUnsafe(LotFilter.class);
                case MIN_NOTIONAL:
                    return SimpleType.constructUnsafe(NotionalFilter.class);
                case PRICE_FILTER:
                    return SimpleType.constructUnsafe(PriceFilter.class);
                default:
                    throw new IOException("Filter " + id + " not recognize");
            }
        }
    }
}
