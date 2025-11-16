import Link from "next/link"

const Header = () => {
    return (
        <header className="fixed w-full h-20 flex items-center bg-gray-800 text-white px-4">
            <nav className="flex items-center flex-row justify-between w-full">
                <Link href="/">Logo</Link>
                <ul className="flex items-center space-x-4">
                    <li>
                        <Link href="/">home</Link>
                    </li>
                    <li>
                        <Link href="/public">public</Link>
                    </li>
                    <li>
                        <Link href="/private">private</Link>
                    </li>
                    <button className="bg-white text-gray-800 font-bold py-2 px-4 rounded">
                        logout
                    </button>
                </ul>
            </nav>
        </header>
    )
}

export { Header }