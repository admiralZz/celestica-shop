// Тип ресурса
export interface Product {
    id: number;
    name: string;
    description: string;
    price: number;
    stockQuantity: number;
    categoryName: string;
    imageId: number;
    imageUrl?: string;
}

// Тип для запроса проверки заказа
export interface ChosenOrderItem {
    productId: number;
    quantity: number;
}


export interface GetOrdersResponse {
    orders: OrderBody[];
}

export interface CheckOrderResponse {
    items: OrderItem[];
    total: number;
}

export interface OrderItem {
    product: Product;
    quantity: number;
    price: number;
}

export interface CreateOrder {
    items: ChosenOrderItem[];
    email: string;
    phone: string;
    address: string;
}

export interface OrderBody {
    items: OrderItem[];
    email: string;
    phone: string;
    address: string;
    total: number;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface LoginResponse {
    email: string;
    ok: boolean;
}
export interface RegisterRequest {
    email: string;
    password: string;
}

export interface AuthResponse {
    id: number;
    email: string;
}