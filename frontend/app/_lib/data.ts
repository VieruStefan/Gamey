import {ProductTypes} from "@/app/_types/product.types";

export const getProducts = async (): Promise<ProductTypes[]> => {
    const response = await fetch(`${process.env.NEXT_PUBLIC_GATEWAY_URL}/gamedata/api/products`);
    return response.json();
};

export const getProduct = async (id: string):
    Promise<ProductTypes> => {
    const response = await fetch(`${process.env.NEXT_PUBLIC_GATEWAY_URL}/gamedata/api/products/${id}`);
    return response.json();
};