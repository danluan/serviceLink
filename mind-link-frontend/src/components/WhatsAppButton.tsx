import { MessageCircle } from "lucide-react";
import { useState } from "react";

const WhatsAppButton = () => {
  const [isHovered, setIsHovered] = useState(false);
  
  const handleClick = () => {
    const phoneNumber = "5511999999999";
    const message = "Olá! Preciso de ajuda.";
    const whatsappUrl = `https://wa.me/${phoneNumber}?text=${encodeURIComponent(message)}`;
    window.open(whatsappUrl, "_blank");
  };

  return (
    <button
      onClick={handleClick}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
      className="fixed bottom-6 right-6 z-50 flex items-center gap-2 rounded-full bg-[#25D366] px-4 py-4 text-white shadow-lg transition-all duration-300 hover:scale-110 hover:shadow-xl"
      aria-label="Ajuda via WhatsApp"
    >
      <MessageCircle className="h-6 w-6" />
      {isHovered && (
        <span className="animate-fade-in text-sm font-medium">
          Ajuda
        </span>
      )}
    </button>
  );
};

export default WhatsAppButton;
