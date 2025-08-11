import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

const resources = {
  en: {
    translation: {
      common: {
        appTitle: 'Celestica Shop'
      },
      header: {
        userMenu: 'User menu',
        profile: 'Profile',
        logout: 'Logout',
        auth: 'Sign In / Sign Up',
        cart: 'Cart'
      },
      footer: {
        tagline: 'Your trusted store for quality products.',
        links: 'Links',
        home: 'Home',
        catalog: 'Catalog',
        about: 'About',
        contacts: 'Contacts',
        contactTitle: 'Contacts',
        address: 'Address: Example st., 123',
        phone: 'Phone: +7 (123) 456-78-90',
        email: 'Email: sales@celestica.com',
        rights: 'All rights reserved.'
      },
      catalog: {
        title: 'Products catalog',
        notFound: 'No products found'
      },
      filters: {
        searchLabel: 'Search products',
        searchPlaceholder: 'Type product name...',
        sortLabel: 'Sort',
        default: 'Default',
        priceAsc: 'Price: low to high',
        priceDesc: 'Price: high to low',
        nameAsc: 'Name: A-Z',
        nameDesc: 'Name: Z-A'
      },
      product: {
        addToCart: 'Add to cart',
        added: 'Added to cart',
        inStock: 'In stock',
        outOfStock: 'Out of stock',
        category: 'Category',
        backToCatalog: '← Back to catalog',
        loading: 'Loading...',
        loadError: 'Failed to load product info',
        notFound: 'Product not found'
      },
      cart: {
        title: 'Cart',
        empty: 'Your cart is empty',
        goCatalog: 'Go to catalog',
        product: 'Product',
        price: 'Price',
        quantity: 'Quantity',
        sum: 'Sum',
        actions: 'Actions',
        decrease: 'Decrease quantity',
        increase: 'Increase quantity',
        remove: 'Remove',
        clear: 'Clear cart',
        total: 'Total',
        checkout: 'Checkout'
      },
      checkout: {
        verifying: 'Checking order...',
        errorBack: 'Back to cart',
        success: 'Order placed successfully!',
        orderDetails: 'Order details',
        position: 'Item',
        price: 'Price',
        quantity: 'Quantity',
        total: 'Total',
        finalTotal: 'Final total',
        email: 'Email',
        phone: 'Phone',
        address: 'Address',
        backCatalog: 'Back to catalog',
        title: 'Checkout',
        yourOrder: 'Your order',
        submit: 'Place order',
        submitting: 'Placing...'
      },
      profile: {
        myOrders: 'My orders',
        settings: 'Settings',
        loading: 'Loading orders...',
        noOrders: 'You have no orders yet.',
        sum: 'Sum'
      },
      activation: {
        confirming: 'Confirming...',
        emailConfirmed: 'Email confirmed! You can sign in now',
        toHome: 'Go to home',
        errorBack: 'Back to home',
        invalidLink: 'Invalid activation link.',
        error: 'Activation error.'
      },
      auth: {
        login: 'Sign in',
        register: 'Sign up',
        email: 'Email',
        password: 'Password',
        confirmPassword: 'Confirm password',
        name: 'Name',
        passwordsMismatch: 'Passwords do not match',
        signingIn: 'Signing in...',
        signIn: 'Sign in',
        registering: 'Registering...',
        signUp: 'Sign up',
        success: 'We sent a confirmation email with a link. If you did not see it, please check Spam.'
      }
    }
  },
  ru: {
    translation: {
      common: {
        appTitle: 'Celestica Shop'
      },
      header: {
        userMenu: 'Пользовательское меню',
        profile: 'Личный кабинет',
        logout: 'Выйти',
        auth: 'Вход/Регистрация',
        cart: 'Корзина'
      },
      footer: {
        tagline: 'Ваш надежный магазин для покупки качественных товаров.',
        links: 'Ссылки',
        home: 'Главная',
        catalog: 'Каталог',
        about: 'О нас',
        contacts: 'Контакты',
        contactTitle: 'Контакты',
        address: 'Адрес: ул. Примерная, 123',
        phone: 'Телефон: +7 (123) 456-78-90',
        email: 'Email: sales@celestica.com',
        rights: 'Все права защищены.'
      },
      catalog: {
        title: 'Каталог товаров',
        notFound: 'Товары не найдены'
      },
      filters: {
        searchLabel: 'Поиск товаров',
        searchPlaceholder: 'Введите название товара...',
        sortLabel: 'Сортировка',
        default: 'По умолчанию',
        priceAsc: 'Цена: по возрастанию',
        priceDesc: 'Цена: по убыванию',
        nameAsc: 'Название: А-Я',
        nameDesc: 'Название: Я-А'
      },
      product: {
        addToCart: 'В корзину',
        added: 'Добавлено в корзину',
        inStock: 'В наличии',
        outOfStock: 'Нет в наличии',
        category: 'Категория',
        backToCatalog: '← Назад в каталог',
        loading: 'Загрузка...',
        loadError: 'Не удалось загрузить информацию о товаре',
        notFound: 'Товар не найден'
      },
      cart: {
        title: 'Корзина',
        empty: 'Ваша корзина пуста',
        goCatalog: 'Перейти в каталог',
        product: 'Товар',
        price: 'Цена',
        quantity: 'Количество',
        sum: 'Сумма',
        actions: 'Действия',
        decrease: 'Уменьшить количество',
        increase: 'Увеличить количество',
        remove: 'Удалить',
        clear: 'Очистить корзину',
        total: 'Итого',
        checkout: 'Оформить заказ'
      },
      checkout: {
        verifying: 'Проверяем заказ...',
        errorBack: 'Вернуться в корзину',
        success: 'Заказ успешно оформлен!',
        orderDetails: 'Детали заказа',
        position: 'Позиция',
        price: 'Цена',
        quantity: 'Количество',
        total: 'Итого',
        finalTotal: 'Итоговая сумма',
        email: 'Email',
        phone: 'Телефон',
        address: 'Адрес',
        backCatalog: 'Вернуться в каталог',
        title: 'Оформление заказа',
        yourOrder: 'Ваш заказ',
        submit: 'Оформить заказ',
        submitting: 'Оформляем...'
      },
      profile: {
        myOrders: 'Мои заказы',
        settings: 'Настройки',
        loading: 'Загрузка заказов...',
        noOrders: 'У вас пока нет заказов.',
        sum: 'Сумма'
      },
      activation: {
        confirming: 'Подтверждение...',
        emailConfirmed: 'Почта успешно подтверждена! Теперь можно входить в ваш аккаунт',
        toHome: 'Перейти на главную',
        errorBack: 'Вернуться на главную',
        invalidLink: 'Некорректная ссылка активации.',
        error: 'Ошибка подтверждения регистрации.'
      },
      auth: {
        login: 'Вход',
        register: 'Регистрация',
        email: 'Email',
        password: 'Пароль',
        confirmPassword: 'Подтвердите пароль',
        name: 'Имя',
        passwordsMismatch: 'Пароли не совпадают',
        signingIn: 'Вход...',
        signIn: 'Войти',
        registering: 'Регистрация...',
        signUp: 'Зарегистрироваться',
        success: 'На вашу электронную почту было отправлено письмо со ссылкой для подтверждения регистрации. Если вы не обнаружили письма, проверьте папку "Спам".'
      }
    }
  },
  中文: {
    translation: {
      common: {
        appTitle: 'Celestica Shop'
      },
      header: {
        userMenu: '用户菜单',
        profile: '个人中心',
        logout: '退出',
        auth: '登录 / 注册',
        cart: '购物车'
      },
      footer: {
        tagline: '您可信赖的优质商品商店。',
        links: '链接',
        home: '首页',
        catalog: '目录',
        about: '关于我们',
        contacts: '联系方式',
        contactTitle: '联系方式',
        address: '地址：示例街 123 号',
        phone: '电话：+7 (123) 456-78-90',
        email: '邮箱：sales@celestica.com',
        rights: '保留所有权利。'
      },
      catalog: {
        title: '商品目录',
        notFound: '未找到商品'
      },
      filters: {
        searchLabel: '搜索商品',
        searchPlaceholder: '输入商品名称...',
        sortLabel: '排序',
        default: '默认',
        priceAsc: '价格：从低到高',
        priceDesc: '价格：从高到低',
        nameAsc: '名称：A-Z',
        nameDesc: '名称：Z-A'
      },
      product: {
        addToCart: '加入购物车',
        added: '已加入购物车',
        inStock: '有货',
        outOfStock: '无货',
        category: '类别',
        backToCatalog: '← 返回目录',
        loading: '加载中...',
        loadError: '无法加载商品信息',
        notFound: '未找到该商品'
      },
      cart: {
        title: '购物车',
        empty: '您的购物车是空的',
        goCatalog: '前往目录',
        product: '商品',
        price: '价格',
        quantity: '数量',
        sum: '小计',
        actions: '操作',
        decrease: '减少数量',
        increase: '增加数量',
        remove: '移除',
        clear: '清空购物车',
        total: '合计',
        checkout: '去结算'
      },
      checkout: {
        verifying: '正在检查订单...',
        errorBack: '返回购物车',
        success: '订单已成功提交！',
        orderDetails: '订单详情',
        position: '商品',
        price: '价格',
        quantity: '数量',
        total: '合计',
        finalTotal: '最终金额',
        email: '邮箱',
        phone: '电话',
        address: '地址',
        backCatalog: '返回目录',
        title: '结算',
        yourOrder: '您的订单',
        submit: '提交订单',
        submitting: '正在提交...'
      },
      profile: {
        myOrders: '我的订单',
        settings: '设置',
        loading: '正在加载订单...',
        noOrders: '您还没有订单。',
        sum: '金额'
      },
      activation: {
        confirming: '正在确认...',
        emailConfirmed: '邮箱验证成功！现在可以登录了',
        toHome: '前往首页',
        errorBack: '返回首页',
        invalidLink: '无效的激活链接。',
        error: '激活失败。'
      },
      auth: {
        login: '登录',
        register: '注册',
        email: '邮箱',
        password: '密码',
        confirmPassword: '确认密码',
        name: '姓名',
        passwordsMismatch: '两次输入的密码不一致',
        signingIn: '正在登录...',
        signIn: '登录',
        registering: '正在注册...',
        signUp: '注册',
        success: '我们已向您的邮箱发送了一封带有链接的确认邮件。如未找到，请检查垃圾邮件文件夹。'
      }
    }
  }
} as const;

const stored = localStorage.getItem('lang');
const nav = navigator.language.toLowerCase();
const fallbackLng = stored || (nav.startsWith('ru') ? 'ru' : nav.startsWith('中文') ? '中文' : 'en');

i18n
  .use(initReactI18next)
  .init({
    resources,
    lng: fallbackLng,
    fallbackLng: 'en',
    interpolation: { escapeValue: false }
  });

export default i18n; 