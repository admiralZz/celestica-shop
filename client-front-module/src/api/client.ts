import * as DTO from './dto';
import {
    mapToProduct,
    mapToCheckOrderResponse,
    mapToOrderItem,
    mapToOrderBody,
    mapToLoginResponse,
    mapToAuthResponse,
    mapToOrderResponse
} from './mapper';
import {client} from "./config";

// Получить все продукты
export const getItems = async (): Promise<DTO.Product[]> => {
    const response = await client.get<DTO.Product[]>('/products');
    return response.data.map(mapToProduct);
};

// Получить все заказы пользователя
export const getMyOrders = async (): Promise<DTO.GetOrdersResponse> => {
    const response = await client.get<DTO.GetOrdersResponse>('/orders', { withCredentials: true });
    return mapToOrderResponse(response.data)
};

// Получить элемент по ID
export const getItemById = async (id: number): Promise<DTO.Product> => {
    const response = await client.get<DTO.Product>(`/products/${id}`);
    return mapToProduct(response.data);
};

// Итоговая проверка заказа
export const checkOrder = async (products: DTO.ChosenOrderItem[]): Promise<DTO.CheckOrderResponse> => {
  const response = await client.post<DTO.CheckOrderResponse>(`/orders/check`, { items: products });
  return mapToCheckOrderResponse(response.data);
};

// Оформление заказа
export const createOrder = async (order: DTO.CreateOrder): Promise<DTO.OrderBody> => {
    const response = await client.post<DTO.OrderBody>(`/orders/create`, order,  { withCredentials: true });
    return mapToOrderBody(response.data);
};

export const login = async (login: DTO.LoginRequest): Promise<DTO.LoginResponse> => {
    const response = await client.post<DTO.LoginRequest>(`/auth/login`, login, { withCredentials: true });
    return mapToLoginResponse(response.data);
}
export const register = async (register: DTO.RegisterRequest): Promise<DTO.AuthResponse> => {
    const response = await client.post<DTO.AuthResponse>(`/auth/register`, register, { withCredentials: true });
    return mapToAuthResponse(response.data);
}
export const me = async (): Promise<DTO.AuthResponse> => {
    const response = await client.get<DTO.AuthResponse>(`/auth/me`, { withCredentials: true });
    return mapToAuthResponse(response.data);
}
export const logout = async (): Promise<void> => {
    try {
        await client.post(`/auth/logout`, {}, { withCredentials: true });
    } catch {}
}

export const confirmEmail = async (token: string): Promise<void> => {
    await client.get(`/auth/confirm?token=${encodeURIComponent(token)}`);
};