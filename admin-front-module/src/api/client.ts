import * as DTO from './dto';
import {
    mapToProduct,
    mapToCheckOrderResponse,
    mapToOrderItem,
    mapToOrderBody,
    mapToLoginResponse,
    mapToAuthResponse,
    mapToOrderResponse, mapToUser
} from './mapper';
import {client} from "./config";

// Получить все продукты
export const getItems = async (): Promise<DTO.Product[]> => {
    const response = await client.get<DTO.Product[]>('/products', { withCredentials: true });
    return response.data.map(mapToProduct);
};

// Получить пользователей
export const getUsers = async (): Promise<DTO.User[]> => {
    const response = await client.get<DTO.User[]>('/users', { withCredentials: true });
    return response.data.map(mapToUser);
};

// Получить все заказы пользователя
export const getOrders = async (): Promise<DTO.GetOrdersResponse> => {
    const response = await client.get<DTO.GetOrdersResponse>('/orders', { withCredentials: true });
    return mapToOrderResponse(response.data)
};

// Получить элемент по ID
export const getItemById = async (id: number): Promise<DTO.Product> => {
    const response = await client.get<DTO.Product>(`/products/${id}`, { withCredentials: true });
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

// Обновить продукт по ID (PUT, multipart/form-data)
export const updateProduct = async (
  id: number,
  data: DTO.UpdateProduct
): Promise<DTO.Product> => {
  const formData = new FormData();
  Object.entries(data).forEach(([key, value]) => {
    if (key === 'image' && value instanceof File) {
      formData.append('image', value);
    } else if (value !== undefined && value !== null) {
      formData.append(key, value as any);
    }
  });
  const response = await client.put<DTO.Product>(`/products/${id}`, formData, {
    withCredentials: true,
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  return mapToProduct(response.data);
};

// Загрузить картинку для продукта
export const uploadProductImage = async (id: number, file: File): Promise<string> => {
  const formData = new FormData();
  formData.append('image', file);
  const response = await client.post(`/products/${id}/image`, formData, {
    withCredentials: true,
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  // Ожидаем, что сервер вернёт { imageUrl: string }
  return response.data.imageUrl;
};

// Создать новый продукт (POST, multipart/form-data)
export const createProduct = async (
  data: DTO.UpdateProduct
): Promise<DTO.Product> => {
  const formData = new FormData();
  Object.entries(data).forEach(([key, value]) => {
    if (key === 'image' && value instanceof File) {
      formData.append('image', value);
    } else if (value !== undefined && value !== null) {
      formData.append(key, value as any);
    }
  });
  const response = await client.post<DTO.Product>(`/products`, formData, {
    withCredentials: true,
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  return mapToProduct(response.data);
};

// Удалить продукт по ID
export const deleteProduct = async (id: number): Promise<void> => {
  await client.delete(`/products/${id}`, { withCredentials: true });
};