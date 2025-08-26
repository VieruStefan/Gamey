import axios from "axios";

const axiosInstance = axios.create({
    baseURL: `${process.env.NEXT_PUBLIC_GATEWAY_URL}`,
});

axiosInstance.interceptors.request.use(
    async (config) => {
        if (!config.headers) {
            config.headers = new axios.AxiosHeaders();
        } else if (!(config.headers instanceof axios.AxiosHeaders)) {
            config.headers = new axios.AxiosHeaders(config.headers);
        }

        return config;
    },
    (error) => Promise.reject(error)
);

export default axiosInstance;