import {getProduct} from "@/app/_lib/data";
import {Suspense, use} from "react";
import Product from "@/app/components/Product/Product";

export default function GamePage(
    {
        params
    }: {
        params: Promise<{ id: string }>
    }) {
    const id = use(params).id;
    const product = getProduct(id);

    return (
        <Suspense fallback={<div>Loading...</div>}>
            <Product product={product}/>
        </Suspense>

    );
}
