import {getGame} from "@/app/_lib/data";
import {Suspense, use} from "react";
import Game from "@/app/components/Product/Game";

export default function GamePage(
    {
        params
    }: {
        params: Promise<{ gameId: string }>
    }) {
    const gameId = use(params).gameId;
    const game = getGame(gameId);

    return (
        <Suspense fallback={<div>Loading...</div>}>
            <Game params={game}/>
        </Suspense>

    );
}
