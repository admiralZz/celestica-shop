// Конфигурация axios
import axios from "axios";

const baseURL = process.env.REACT_APP_API_BASE_URL;
console.log("[API] baseURL =", baseURL);

export const client = axios.create({
    baseURL,
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
    },
});