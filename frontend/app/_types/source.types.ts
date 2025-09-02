import {PriceHistoryTypes} from "@/app/_types/price-history.types";

export interface SourceTypes {
    url: string
    platform: string
    priceHistory: PriceHistoryTypes[]
}