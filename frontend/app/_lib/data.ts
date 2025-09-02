import type {GameTypes, Page} from "@/app/_types/gameTypes"

const getReq = (page: number, size: number, search?: string) => {
    let req = `page=${page}&size=${size}`
    if (search !== undefined && search !== "") {
        req += `&search=${search}`
    }
    return req
}

export const getGames = async (req: any): Promise<Page<GameTypes>> => {
    const gatewayUrl = process.env.NEXT_PUBLIC_GATEWAY_URL
    const response = await fetch(`${gatewayUrl}/gamedata/api/products?${getReq(req["page"], req["size"], req["search"])}`)

    return await response.json()
}

export const getGame = async (gameId: string): Promise<GameTypes | null> => {
    const gatewayUrl = process.env.NEXT_PUBLIC_GATEWAY_URL
    const response = await fetch(`${gatewayUrl}/gamedata/api/products/${gameId}`)

    return await response.json()
}
