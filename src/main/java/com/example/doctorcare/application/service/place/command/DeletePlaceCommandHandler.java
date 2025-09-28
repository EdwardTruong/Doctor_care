package com.example.doctorcare.application.service.place.command;

@CqrsCommandHandler(DeletePlaceCommand.class)
@RequiredArgsConstructor
public class DeleteplaceCommandHandler implements CommandHandler<DeletePlaceCommand> {
       private final PlaceRepository placeRepository;

    @Override
    @Transactional
    public void handle(DeletePlaceCommand command) {

        if (!placeRepository.existsById(command.id())) {
            throw new ResourceNotFoundException("Không tìm thấy vùng với ID", "Id", command.id());
        }
        
        membershipRepository.deleteById(command.id());
    }
}
