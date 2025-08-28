import {getProducts} from "@/app/_lib/data";
import {Suspense} from "react";
import Loading from "./loading";
import Products from "@/app/components/Products/Products";

export default function HomePage() {

    const products = getProducts();

    return (
        <Suspense fallback={<Loading/>}>
            <Products products={products}/>
        </Suspense>
    );
}
