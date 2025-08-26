import {ProductTypes} from "@/_types/product.types";
import axios from "./axios";

export const getProducts = async (): Promise<ProductTypes[]> => {
    const response = await axios.get<ProductTypes[]>("/gamedata/api/products");
    return response.data;
};

export const getProduct = async (id: string):
    Promise<ProductTypes> => {
    const response = await axios.get<ProductTypes>(`/gamedata/api/products/${id}`);
    return response.data;
};