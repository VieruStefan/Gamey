import {SourceTypes} from "@/app/_types/source.types";

export interface Page<T> {
    content: T[]
    totalPages: number;
    totalElements: number
    size: number
    number: number
    first: boolean
    last: boolean
}

export interface GameTypes {
    gameId: string
    title: string
    image: string
    sources: SourceTypes[]
}
