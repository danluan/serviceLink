import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";

interface Service {
  id: number;
  name: string;
  description: string;
  price: string;
  category: string;
}

interface ServiceCardProps {
  service: Service;
}

const ServiceCard = ({ service }: ServiceCardProps) => {
  return (
    <Card className="flex flex-col h-full hover:shadow-hover transition-shadow">
      <CardHeader>
        <div className="flex items-start justify-between gap-2 mb-2">
          <CardTitle className="text-xl">{service.name}</CardTitle>
          <Badge variant="secondary" className="shrink-0">
            {service.category}
          </Badge>
        </div>
        <CardDescription>{service.description}</CardDescription>
      </CardHeader>
      <CardContent className="flex-grow">
        <div className="text-2xl font-bold text-primary">
          {service.price}
        </div>
      </CardContent>
      <CardFooter>
        <Button className="w-full">
          Contrate Já
        </Button>
      </CardFooter>
    </Card>
  );
};

export default ServiceCard;
